package gui;

import app.Commande;

public interface IMyCommandeDialogOwner extends IMyDialogOwner {
    /**
     * The return of a CommandeDialog
     * @param commande the order associated
     */
    public void commandeDialogReturn(Commande commande);
}
