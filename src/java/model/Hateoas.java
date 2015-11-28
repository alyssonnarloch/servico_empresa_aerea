package model;

public class Hateoas {

    public final static String ITEM = "item";
    public final static String COLLECTION = "collection";
    public final static String EDIT = "edit";
    public final static String LATEST_VERSION = "latest-version";
    public final static String SELF = "self";

    private String rel;
    private String href;
    private String title;
    private String method;
    private String type;

    public Hateoas() {
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
