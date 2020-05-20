package gui;

import app.Client;

public interface IMyUserDialogOwner extends IMyDialogOwner {
    /**
     * The return of a UserDialog
     * @param client the client associated
     */
    public void userDialogReturn(Client client);
}
