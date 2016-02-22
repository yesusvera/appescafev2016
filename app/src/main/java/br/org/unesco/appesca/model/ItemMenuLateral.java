package br.org.unesco.appesca.model;

/**
 * Created by yesusvera on 28/11/15.
 */
public class ItemMenuLateral {

    public final int  id;
    public final String title;
    public final int id_layout_inflate;

    public ItemMenuLateral(int id, String title, int id_layout_inflate) {
        this.id = id;
        this.title = title;
        this.id_layout_inflate= id_layout_inflate;
    }

    @Override
    public String toString() {
        return title;
    }
}