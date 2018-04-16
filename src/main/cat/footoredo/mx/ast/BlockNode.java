package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends StatementNode {
    protected Location location;
    private List<BlockStatementNode> blockStatements;

    BlockNode (Location location) {
        super ();
        this.location = location;
        this.blockStatements = new ArrayList<>();
    }

    public void addBlockStatement (BlockStatementNode blockStatement) {
        blockStatements.add (blockStatement);
    }

    public List<BlockStatementNode> getBlockStatements() {
        return blockStatements;
    }

    public Location getLocation() {
        return location;
    }
}
