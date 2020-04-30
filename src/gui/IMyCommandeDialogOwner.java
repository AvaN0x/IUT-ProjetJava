package gui;

import app.Commande;

public interface IMyCommandeDialogOwner extends IMyDialogOwner {
    public void commandeDialogReturn(Commande commande);
}
