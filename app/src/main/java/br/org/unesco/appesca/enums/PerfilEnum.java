package br.org.unesco.appesca.enums;

public enum PerfilEnum {

    ADMINISTRADOR(0, "Administrador", "fa-user-secret"),
    COORDENADOR(1, "Coordenador", "fa-users"),
    DEGRAVADOR(2, "Degravador", "fa-file-audio-o"),
    PESQUISADOR(3, "Pesquisador", "fa-street-view");

    private final int valor;
    private final String desc;
    private final String fontAweSomeIcon;

    private PerfilEnum(int valor, String desc, String fontAweSomeIcon) {
        this.valor = valor;
        this.desc = desc;
        this.fontAweSomeIcon = fontAweSomeIcon;
    }

    public int getValor(){
        return valor;
    }

    public String getDescricao(){
        return desc;
    }

    public String getLabelSelect(){
        return "<i class='fa #{usr.perfil.fontAweSomeIcon} fa-2x'/>"+desc;
    }

    public String getFontAweSomeIcon(){
        return fontAweSomeIcon;
    }
}
