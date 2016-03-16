package br.org.unesco.appesca.model;

import java.io.Serializable;

/**
 * Created by yesus on 1/24/16.
 */
public abstract class BaseModel implements Serializable, Cloneable {

    public abstract Integer getId();

    public abstract void setId(Integer id);
}
