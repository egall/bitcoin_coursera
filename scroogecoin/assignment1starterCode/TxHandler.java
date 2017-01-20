import java.util.ArrayList;
import java.util.Arrays;
public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    private UTXOPool txhUtxoPool;
    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
        txhUtxoPool = new UTXOPool(utxoPool);
        utxoPool.getAllUTXO();
        System.out.println("In TxHandler");
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        int itor;
        double outVal;
        int numIn = 0;
        int numOut = 0;

        double inputSum = 0;
        double outputSum = 0;

        UTXOPool spentPool = new UTXOPool();
        ArrayList<UTXO> spentList;

        numIn = tx.numInputs();
        numOut = tx.numOutputs();

        Transaction.Input in;
        Transaction.Output out;

        // If either Input or Output list is empty transaction is invalid
        if (numIn <= 0 || numOut <= 0) return false;

        /* Run through all outputs keep track of total value */
        for (itor = 0; itor < numOut; itor++) { 
            out = tx.getOutput(itor);
            outVal = out.value;
            // Rule #4
            if (outVal < 0) return false;
            outputSum += outVal;
        }

        /* Run through all inputs */
        for(itor = 0; itor < numIn; itor++){
            in = tx.getInput(itor);
            /* Our input is a former output which is stored in the UTXOPool, get input from UTXO pool */
            UTXO ut = new UTXO(in.prevTxHash, in.outputIndex);
            Transaction.Output inUt = txhUtxoPool.getTxOutput(ut);

            // Rule #1
            if (inUt == null) return false;
            inputSum += inUt.value;
            
            // Rule #2
            if (!Crypto.verifySignature (inUt.address, tx.getRawDataToSign(itor), in.signature) ) return false;

            // Rule #3
            if (spentPool.contains(ut)) return false;
            spentPool.addUTXO(ut, inUt);

        } 
        // Rule #5
        if (inputSum < outputSum) return false;

        return true;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        ArrayList<Transaction> txBlock = new ArrayList();

        for (Transaction tx : possibleTxs) {
            if (!txBlock.contains(tx)) {
                if (isValidTx(tx) ) {
                    for (Transaction.Input in : tx.getInputs() ) {
                        UTXO ut = new UTXO(in.prevTxHash, in.outputIndex);
                        txhUtxoPool.removeUTXO(ut);
                    }
                    int idx = 0;
                    for (Transaction.Output out : tx.getOutputs() ) {
                         UTXO ut = new UTXO(tx.getHash(), idx++);
                         txhUtxoPool.addUTXO(ut, out);
                    }
                    txBlock.add(tx);
                }
            } 
        } 
        System.out.println("In handleTxs");
        Transaction[] transactionBlk = txBlock.toArray(new Transaction[txBlock.size()]);
        //return txBlock.toArray(new Transaction[txBlock.size()]);
        return transactionBlk;
    }

}
