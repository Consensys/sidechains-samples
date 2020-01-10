/*
 * Copyright 2018 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
pragma solidity ^0.5.0;

/**
 * Verify BLS Threshold Signed values.
 *
 * Much of the code in this file is derived from here:
 * https://github.com/kfichter/solidity-bls/blob/master/contracts/BLS.sol
 */
contract SignatureVerification {
    uint8 constant private MAX_ATTEMPTS_AT_HASH_TO_CURVE = 10;

    struct E1Point {
        uint x;
        uint y;
    }

    struct E2Point {
        uint[2] x;
        uint[2] y;
    }


    /**
     * Checks if a BLS signature is valid.
     *
     * @param _verificationKey Public verification key associated with the secret key that signed the message.
     * @param _message Message that was signed.
     * @param _signature Signature over the message.
     * @return True if the message was correctly signed.
     */
    function verify(
        E2Point _publicKey,
        bytes calldata _message,
        E1Point _signature
    ) internal returns (bool) {
        E1Point memory messageHash = Pairing.hashToCurveE1(_message);
        return pairing2(negate(_signature), G2(), messageHash, _publicKey);
    }


    /**
     * @return The generator of E1.
     */
    function G1() internal pure returns (E1Point) {
        return E1Point(1, 2);
    }

    /**
     * @return The generator of E2.
     */
    function G2() internal pure returns (E2Point) {
        return E2Point({
            x: [11559732032986387107991004021392285783925812861821192530917403151452391805634,
            10857046999023057135944570762232829481370756359578518086990519993285655852781],
            y: [4082367875863433681332203403145435568316851327593401208105741076214120093531,
            8495653923123431417604973247489272438418190587263600148770280649306958101930]
            });
    }


    function hashToCurveE1(bytes calldata _data) internal pure returns (uint256[2]) {
        // Security Domain: BN128
        bytes memory securityDomain = hex"424E313238";
        var toBeHashed = securityDomain.concat(_data);
        bytes digest = keccak256(toBeHashed);
        return mapToCurveE1(digest);
    }

    function mapToCurveE1(uint256 _hash) private pure returns (uint256[2]) {
        uint8 ctr = 0;
        while (true) {
            uint256 x = _hash + ctr;
            // Don't worry about making the value mod q. This will be done as part of the scalar multiply.

            // Scalar multiply value by base point.
            p = curveMul(G1(), x); // map to point

            // if map is valid, we are done
            if (!isAtInfinity(p)) {
                break;
            }

            // bump counter for next round, if necessary
            ctr++;
            require(ctr < MAX_ATTEMPTS_AT_HASH_TO_CURVE, "Failed to map to point");
        }
        return (p);
    }

    /**
     * Negate a point: Assuming the point isn't at infinity, the negatation is same x value with -y.
     *
     * @dev Negates a point in E1.
     * @param _point Point to negate.
     * @return The negated point.
     */
    function negate(E1Point _point) internal pure returns (E1Point) {
        // Field Modulus.
        uint q = 21888242871839275222246405745257275088696311157297823662689037894645226208583;
        if (isAtInfinity(_point)) {
            return E1Point(0, 0);
        }
        return E1Point(_point.x, q - (_point.y % q));
    }

    /**
     * Computes the pairing check e(p1[0], p2[0]) *  .... * e(p1[n], p2[n]) == 1
     *
     * @param _e1points List of points in E1.
     * @param _e2points List of points in E2.
     * @return True if pairing check succeeds.
     */
    function pairing(E1Point[] _e1points, E2Point[] _e2points) internal returns (bool) {
        require(_e1points.length == _e2points.length, "Point count mismatch.");

        uint elements = _e1points.length;
        uint inputSize = elements * 6;
        uint[] memory input = new uint[](inputSize);

        for (uint i = 0; i < elements; i++) {
            input[i * 6 + 0] = _e1points[i].x;
            input[i * 6 + 1] = _e1points[i].y;
            input[i * 6 + 2] = _e2points[i].x[0];
            input[i * 6 + 3] = _e2points[i].x[1];
            input[i * 6 + 4] = _e2points[i].y[0];
            input[i * 6 + 5] = _e2points[i].y[1];
        }

        uint[1] memory out;
        bool success;

        assembly {
            success := call(sub(gas, 2000), 8, 0, add(input, 0x20), mul(inputSize, 0x20), out, 0x20)
        }
        require(success, "Pairing operation failed.");

        return out[0] != 0;
    }

    /**
     * @dev Convenience method for pairing check on two pairs.
     * @param _g1point1 First point in G1.
     * @param _g2point1 First point in G2.
     * @param _g1point2 Second point in G1.
     * @param _g2point2 Second point in G2.
     * @return True if the pairing check succeeds.
     */
    function pairing2(
        E1Point _g1point1,
        E2Point _g2point1,
        E1Point _g1point2,
        E2Point _g2point2
    ) internal returns (bool) {
        E1Point[] memory g1points = new G1Point[](2);
        E2Point[] memory g2points = new G2Point[](2);
        g1points[0] = _g1point1;
        g1points[1] = _g1point2;
        g2points[0] = _g2point1;
        g2points[1] = _g2point2;
        return pairing(g1points, g2points);
    }


    /*
     * Private functions
     */

    /**
     * @dev Multiplies a point in G1 by a scalar.
     * @param _point G1 point to multiply.
     * @param _scalar Scalar to multiply.
     * @return The resulting G1 point.
     */
    function curveMul(E1Point _point, uint _scalar) private returns (E1Point) {
        uint[3] memory input;
        input[0] = _point.x;
        input[1] = _point.y;
        input[2] = _scalar;

        bool success;
        E1Point memory result;
        assembly {
            success := call(sub(gas, 2000), 7, 0, input, 0x80, result, 0x60)
        }
        require(success, "Point multiplication failed.");

        return result;
    }

    function isAtInfinity(E1Point _point) private pure returns (boolean){
        return (_point.x == 0 && _point.y == 0);
    }


}
