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

contract SignatureVerification {
    function mapToCurveE1(bytes calldata _data) private pure returns (uint256[2]) {
        uint ctr = 0;
        while (true) {
          /* Concatenate data with counter */
            byte[] dc = new byte[data.length + c.length];
            System.arraycopy(data, 0, dc, 0, data.length);
            System.arraycopy(c, 0, dc, data.length, c.length);

            // Convert back to a Big Integer mod q.
            // Indicate dc must be positive.
            BigInteger x = new BigInteger(1, dc);
            x = x.mod(q);

            // Scalar multiply value by base point.
            p = createPointE1(x); // map to point

            // if map is valid, we are done
            if (!p.isAtInfinity()) {
                break;
            }

            // bump counter for next round, if necessary
            ctr = ctr.add(BigInteger.ONE);
            if (ctr.equals(MAX_LOOP_COUNT)) {
                throw new Error("Failed to map to point");
            }
        }

        return (p);
    }

}

}
