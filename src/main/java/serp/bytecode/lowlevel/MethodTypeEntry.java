package serp.bytecode.lowlevel;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import serp.bytecode.visitor.BCVisitor;

/**
 * MethodType 
 *  u1 tag 
 *  u2 descriptor_index
 *
 */
public class MethodTypeEntry extends Entry {
    private int _descriptor_index;      // Must ref a UTF8Entry representing a method descriptor

    public MethodTypeEntry() {

    }

    public MethodTypeEntry(int _descriptor_index) {
        this._descriptor_index = _descriptor_index;
    }

    public void acceptVisit(BCVisitor visitor) {
        visitor.enterMethodTypeEntry(this);
        visitor.exitMethodTypeEntry(this);
    }

    public int getType() {
        return Entry.METHODTYPE;
    }

    void readData(DataInput in) throws IOException {
        _descriptor_index = in.readUnsignedShort();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeShort(_descriptor_index);
    }

    public UTF8Entry getMethodDescriptorEntry() {
        return (UTF8Entry) getPool().getEntry(_descriptor_index);
    }
}
