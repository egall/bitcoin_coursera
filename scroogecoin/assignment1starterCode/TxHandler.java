import java.util.ArrayList;
import java.util.Arrays;
public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    private UTXOPool utxoPoolTxH;
    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
        utxoPoolTxH = new UTXOPool(utxoPool);
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

        numIn = tx.numInputs();
        numOut = tx.numOutputs();

        System.out.println("Num in = " + numIn + " numout = " + numOut);

        // If either Input or Output list is empty transaction is invalid
        if (numIn <= 0 || numOut <= 0) return false;

        for (itor = 0; itor < numOut; itor++) { 
            outVal = tx.getOutput(itor).value;
            System.out.println("Currval = " + outVal);
            if (outVal < 0) return false;
            outputSum += outVal;
        }
        System.out.println("output sum = " + outputSum);


        for(itor = 0; itor < numIn; itor++){
            System.out.println("itor = " + itor);
            //UTXO ut = new UTXO(tx.getInputs(itor), 
        } 
        return true;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
        Transaction[] t = new Transaction[16];
        System.out.println("In handleTxs");
        return t;
    }

}
