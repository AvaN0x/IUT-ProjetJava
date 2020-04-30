package gui;

import app.Commande;

public interface IMyCommandeDialog extends IMyDialogOwner {
    public void commandeDialogReturn(Commande commande);
}
