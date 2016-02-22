package br.org.unesco.appesca.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuContent {

    public static final List<ItemMenuLateral> ITEMS = new ArrayList<ItemMenuLateral>();
    public static final Map<Integer, ItemMenuLateral> ITEM_MAP = new HashMap<Integer, ItemMenuLateral>();


    static {
            addItem(new ItemMenuLateral(1, "Todos",1)); //TODO
            addItem(new ItemMenuLateral(2, "Enviados", 2)); //TODO
            addItem(new ItemMenuLateral(3, "Não Enviados",3)); //TODO
    }

    private static void addItem(ItemMenuLateral item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
