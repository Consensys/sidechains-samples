pragma solidity ^0.5.0;

import "./IERC20.sol";
import "./ERC20LockableAccount.sol";
import "../../../../common-solidity/crosschain-precompile-calls/contracts/Crosschain.sol";

/**
 * @dev Implementation of the {IERC20} interface.
 *
 * The balances are stored in their own contracts, to allow for the router / lockable item pattern.
 *
 * This implementation is agnostic to the way tokens are created. This means
 * that a supply mechanism has to be added in a derived contract using {_mint}.
 * For a generic mechanism see {ERC20Mintable}.
 *
 * TIP: For a detailed writeup see our guide
 * https://forum.zeppelin.solutions/t/how-to-implement-erc20-supply-mechanisms/226[How
 * to implement supply mechanisms].
 *
 * We have followed general OpenZeppelin guidelines: functions revert instead
 * of returning `false` on failure. This behavior is nonetheless conventional
 * and does not conflict with the expectations of ERC20 applications.
 *
 * Additionally, an {Approval} event is emitted on calls to {transferFrom}.
 * This allows applications to reconstruct the allowance for all accounts just
 * by listening to said events. Other implementations of the EIP may not emit
 * these events, as it isn't required by the specification.
 *
 * Finally, the non-standard {decreaseAllowance} and {increaseAllowance}
 * functions have been added to mitigate the well-known issues around setting
 * allowances. See {IERC20-approve}.
 */
contract ERC20Router is IERC20, Crosschain {
    mapping (address => address[]) private lockableAccounts;

    mapping (address => mapping (address => uint256)) private allowances;

    uint256 private theTotalSupply;

    address public owner;

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    constructor() public {
        owner = msg.sender;
    }

    /**
     * @dev See {IERC20-totalSupply}.
     */
    function totalSupply() public view returns (uint256) {
        return theTotalSupply;
    }



    /**
     * Only return the balance of the [0] account.
     */
    function balanceOf(address account) public view returns (uint256) {
        address[] memory lockableAccountAddresses = lockableAccounts[account];
        if (lockableAccountAddresses.length == 0) {
            return 0;
        }
        ERC20LockableAccount lockableAccount = ERC20LockableAccount(lockableAccountAddresses[0]);
        return lockableAccount.balance();
    }

    /**
     * Move all unlocked accounts into the [0] account.
     */
    function condense(address _account) public {
        address[] memory lockableAccountAddresses = lockableAccounts[_account];
        require(lockableAccountAddresses.length != 0);
        uint256 total = 0;
        for (uint256 i=1; i< lockableAccountAddresses.length; i++) {
            ERC20LockableAccount lockableAccount = ERC20LockableAccount(lockableAccountAddresses[i]);
            // TODO if account not locked.
            total += lockableAccount.withdrawAll();
        }
        ERC20LockableAccount rootLockableAccount = ERC20LockableAccount(lockableAccountAddresses[0]);
        rootLockableAccount.add(total);
    }

    function createAccount(address[] memory _lockableAccountContracts) public {
        for (uint256 i=0; i<_lockableAccountContracts.length; i++) {
            ERC20LockableAccount lockableAccount = ERC20LockableAccount(_lockableAccountContracts[i]);
            require(lockableAccount.owner() == msg.sender);
            lockableAccounts[msg.sender].push(_lockableAccountContracts[i]);
        }
    }

    function createAccountFor(address account, address[] calldata _lockableAccountContracts) external onlyOwner{
        for (uint256 i=0; i<_lockableAccountContracts.length; i++) {
            ERC20LockableAccount lockableAccount = ERC20LockableAccount(_lockableAccountContracts[i]);
            require(lockableAccount.owner() == account);
            lockableAccounts[msg.sender].push(_lockableAccountContracts[i]);
        }
    }

    /**
     * @dev See {IERC20-transfer}.
     *
     * Requirements:
     *
     * - `recipient` cannot be the zero address.
     * - the caller must have a balance of at least `amount`.
     */
    function transfer(address _recipient, uint256 _amount) public returns (bool) {
        _transfer(msg.sender, _recipient, _amount);
        return true;
    }

    /**
     * @dev See {IERC20-allowance}.
     */
    function allowance(address _owner, address _spender) public view returns (uint256) {
        return allowances[_owner][_spender];
    }

    /**
     * @dev See {IERC20-approve}.
     *
     * Requirements:
     *
     * - `spender` cannot be the zero address.
     */
    function approve(address _spender, uint256 _amount) public returns (bool) {
        _approve(msg.sender, _spender, _amount);
        return true;
    }

    /**
     * @dev See {IERC20-transferFrom}.
     *
     * Emits an {Approval} event indicating the updated allowance. This is not
     * required by the EIP. See the note at the beginning of {ERC20};
     *
     * Requirements:
     * - `sender` and `recipient` cannot be the zero address.
     * - `sender` must have a balance of at least `amount`.
     * - the caller must have allowance for `sender`'s tokens of at least
     * `amount`.
     */
    function transferFrom(address _sender, address _recipient, uint256 _amount) public returns (bool) {
        _transfer(_sender, _recipient, _amount);
//TODO
//        uint256 accAllowance = allowances[_sender][msg.sender];
//        require(accAllowance >= _amount, "ERC20: transfer amount exceeds allowance");
//        _approve(_sender, msg.sender, accAllowance - _amount);
        return true;
    }

    /**
     * @dev Atomically increases the allowance granted to `spender` by the caller.
     *
     * This is an alternative to {approve} that can be used as a mitigation for
     * problems described in {IERC20-approve}.
     *
     * Emits an {Approval} event indicating the updated allowance.
     *
     * Requirements:
     *
     * - `spender` cannot be the zero address.
     */
    function increaseAllowance(address _spender, uint256 _addedValue) public returns (bool) {
        uint256 accAllowance = allowances[_spender][msg.sender];
        _approve(msg.sender, _spender, accAllowance + _addedValue);
        return true;
    }

    /**
     * @dev Atomically decreases the allowance granted to `spender` by the caller.
     *
     * This is an alternative to {approve} that can be used as a mitigation for
     * problems described in {IERC20-approve}.
     *
     * Emits an {Approval} event indicating the updated allowance.
     *
     * Requirements:
     *
     * - `spender` cannot be the zero address.
     * - `spender` must have allowance for the caller of at least
     * `subtractedValue`.
     */
    function decreaseAllowance(address _spender, uint256 _subtractedValue) public returns (bool) {
        uint256 accAllowance = allowances[msg.sender][_spender];
        require(accAllowance >= _subtractedValue, "ERC20: decreased allowance below zero");
        _approve(msg.sender, _spender, accAllowance - _subtractedValue);
        return true;
    }

    // Some functions to help debug

    function accSize(address _acc) external view returns (uint256) {
        address[] memory lockableAccountAddresses = lockableAccounts[_acc];
        return lockableAccountAddresses.length;
    }

    function getLockableAccountAddress(address _account, uint256 _instance) public view returns (address) {
        return lockableAccounts[_account][_instance];
    }
    function accGetBalance(address _acc, uint256 _index) external view returns (uint256) {
        address[] memory lockableAccountAddresses = lockableAccounts[_acc];
        ERC20LockableAccount lockableAccount = ERC20LockableAccount(lockableAccountAddresses[_index]);
        return lockableAccount.balance();
    }
    function accGetOwner(address _acc, uint256 _index) external view returns (address) {
        address[] memory lockableAccountAddresses = lockableAccounts[_acc];
        ERC20LockableAccount lockableAccount = ERC20LockableAccount(lockableAccountAddresses[_index]);
        return lockableAccount.owner();
    }
    function accGetRouter(address _acc, uint256 _index) external view returns (address) {
        address[] memory lockableAccountAddresses = lockableAccounts[_acc];
        ERC20LockableAccount lockableAccount = ERC20LockableAccount(lockableAccountAddresses[_index]);
        return lockableAccount.erc20RouterContract();
    }

    /**
     * @dev Moves tokens `amount` from `sender` to `recipient`.
     * Moves from sender[0] to an instance of recipient that is unlocked.
     *
     * This is internal function is equivalent to {transfer}, and can be used to
     * e.g. implement automatic token fees, slashing mechanisms, etc.
     *
     * Emits a {Transfer} event.
     *
     * Requirements:
     *
     * - `sender` cannot be the zero address.
     * - `recipient` cannot be the zero address.
     * - `sender` must have a balance of at least `amount`.
     */
    function _transfer(address _sender, address _recipient, uint256 _amount) internal {
        require(_sender != address(0), "ERC20: transfer from the zero address");
        require(_recipient != address(0), "ERC20: transfer to the zero address");

        address[] memory senderLockableAccountAddresses = lockableAccounts[_sender];
        require(senderLockableAccountAddresses.length != 0, "ERC20: no sender lockable contract");
        ERC20LockableAccount senderLockableAccount = ERC20LockableAccount(senderLockableAccountAddresses[0]);

        address[] memory recipientLockableAccountAddresses = lockableAccounts[_recipient];
        require(recipientLockableAccountAddresses.length != 0);
        ERC20LockableAccount recipientLockableAccount;
//        for (uint256 i=0; i<recipientLockableAccountAddresses.length; i++) {
//            if (!crosschainIsLocked(recipientLockableAccountAddresses[i])) {
//                recipientLockableAccount = ERC20LockableAccount(recipientLockableAccountAddresses[i]);
        recipientLockableAccount = ERC20LockableAccount(recipientLockableAccountAddresses[0]);
//                break;
//            }
  //      }
        require(address(recipientLockableAccount) != address(0));
        senderLockableAccount.sub(_amount);
        recipientLockableAccount.add(_amount);
        emit Transfer(_sender, _recipient, _amount);
    }



    /** @dev Creates `amount` tokens and assigns them to `account`, increasing
     * the total supply.
     *
     * Emits a {Transfer} event with `from` set to the zero address.
     *
     * Requirements
     *
     * - `to` cannot be the zero address.
     */
    function mint(uint256 _amount) onlyOwner public {
        theTotalSupply = theTotalSupply + _amount;

        address[] memory recipientLockableAccountAddresses = lockableAccounts[msg.sender];
        require(recipientLockableAccountAddresses.length != 0);
        ERC20LockableAccount recipientLockableAccount = ERC20LockableAccount(recipientLockableAccountAddresses[0]);
        recipientLockableAccount.add(_amount);
        emit Transfer(address(0), msg.sender, _amount);
    }



//    /**
//     * @dev Destroys `amount` tokens from `account`, reducing the
//     * total supply.
//     *
//     * Emits a {Transfer} event with `to` set to the zero address.
//     *
//     * Requirements
//     *
//     * - `account` cannot be the zero address.
//     * - `account` must have at least `amount` tokens.
//     */
//    function _burn(address _account, uint256 _amount) internal {
//        require(_account != address(0), "ERC20: burn from the zero address");
//
//        address recipientLockableAccountAddress = lockableAccounts[_account];
//        if (receiverLockableAccountAddress == address(0)) {
//            lockableAccounts[_account] = recipientLockableAccountAddress = new ERC20LockableAccount(this);
//        }
//        ERC20LockableAccount recipientLockableAccount = ERC20LockableAccount(recipientLockableAccountAddress);
//        recipientLockableAccount.sub(_amount);
//        totalSupply = totalSupply.sub(_amount);
//        emit Transfer(_account, address(0), _amount);
//    }

    /**
     * @dev Sets `amount` as the allowance of `spender` over the `owner`s tokens.
     *
     * This is internal function is equivalent to `approve`, and can be used to
     * e.g. set automatic allowances for certain subsystems, etc.
     *
     * Emits an {Approval} event.
     *
     * Requirements:
     *
     * - `owner` cannot be the zero address.
     * - `spender` cannot be the zero address.
     */
    function _approve(address _owner, address _spender, uint256 _amount) internal {
        require(_owner != address(0), "ERC20: approve from the zero address");
        require(_spender != address(0), "ERC20: approve to the zero address");

        allowances[_owner][_spender] = _amount;
        emit Approval(_owner, _spender, _amount);
    }

//    /**
//     * @dev Destroys `amount` tokens from `account`.`amount` is then deducted
//     * from the caller's allowance.
//     *
//     * See {_burn} and {_approve}.
//     */
//    function _burnFrom(address account, uint256 amount) internal {
//        _burn(account, amount);
//        _approve(account, _msgSender(), allowances[account][_msgSender()].sub(amount, "ERC20: burn amount exceeds allowance"));
//    }
}
