package gui;

import app.Produit;

public interface IMyProduitDialogOwner extends IMyDialogOwner {
    /**
     * The return of a ProduitDialog
     * @param produit the product associated
     */
    public void produitDialogReturn(Produit produit);
}
