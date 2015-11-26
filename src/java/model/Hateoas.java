package model;

import java.util.ArrayList;
import java.util.List;

public class Hateoas {

    protected List<Link> links;

    public Hateoas() {
        this.links = new ArrayList<Link>();
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
    
    public void addLink(Link link) {
        this.links.add(link);
    }
}
