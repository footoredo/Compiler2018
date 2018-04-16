package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ContinueNode extends StatementNode {
    protected Location location;

    public ContinueNode(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
