package serp.bytecode.lowlevel;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import serp.bytecode.visitor.BCVisitor;

/**
 * InvokeDynamic
 *    u1 tag
 *    u2 bootstrap_method_attr_index    // References entry in Bootstrap Methods table
 *    u2 name_and_type_index            // References NameAndTypeEntry representing method name and descriptor
 *
 */
public class InvokeDynamicEntry extends Entry {	
    private int _bootstrap_method_attr_index;
    private int _name_and_type_index;
    
	public InvokeDynamicEntry() {
		
	}
	
	public InvokeDynamicEntry(int bootstrap_method_attr_index, int nameAndTypeIndex) {
        _bootstrap_method_attr_index = bootstrap_method_attr_index;
        _name_and_type_index = nameAndTypeIndex;
    }
	
	public void acceptVisit(BCVisitor visitor) {
		visitor.enterInvokeDynamicEntry(this);
		visitor.exitInvokeDynamicEntry(this);
	}

	public int getType() {
		return Entry.INVOKEDYNAMIC;
	}

    void readData(DataInput in) throws IOException {
        _bootstrap_method_attr_index = in.readUnsignedShort();
        _name_and_type_index = in.readUnsignedShort();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeShort(_bootstrap_method_attr_index);
        out.writeShort(_name_and_type_index);
    }
    
    public int getBootstrapMethodAttrIndex() {
        return _bootstrap_method_attr_index;
    }
    
    /**
     * Return the constant pool index of the {@link NameAndTypeEntry}
     * describing this entity.
     */
    public int getNameAndTypeIndex() {
        return _name_and_type_index;
    }
    /**
     * Return the referenced {@link NameAndTypeEntry}. This method can only
     * be run for entries that have been added to a constant pool.
     */
    public NameAndTypeEntry getNameAndTypeEntry() {
        return (NameAndTypeEntry) getPool().getEntry(_name_and_type_index);
    }
}
