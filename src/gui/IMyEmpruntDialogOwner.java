package gui;

import app.Emprunt;

public interface IMyEmpruntDialogOwner extends IMyDialogOwner {
    /**
     * The return of a EmpruntDialog
     * @param emprunt the loan associated
     */
    public void empruntDialogReturn(Emprunt emprunt);
}
