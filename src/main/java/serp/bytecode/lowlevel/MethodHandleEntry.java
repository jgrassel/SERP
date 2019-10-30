package serp.bytecode.lowlevel;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import serp.bytecode.visitor.BCVisitor;

/**
 * MethodHandle
 *   u1 tag 
 *   u1 reference_kind 
 *   u2 reference_index
 *
 */
public class MethodHandleEntry extends Entry {
    private int _reference_kind = 0;
    private int _reference_index = 0;
    
    public MethodHandleEntry() {

    }

    public MethodHandleEntry(int _reference_kind, int _reference_index) {
        this._reference_kind = _reference_kind;
        this._reference_index = _reference_index;
    }

    public void acceptVisit(BCVisitor visitor) {
        visitor.enterMethodHandleEntry(this);
        visitor.exitMethodHandleEntry(this);
    }

    public int getType() {
        return Entry.METHODHANDLE;
    }

    void readData(DataInput in) throws IOException {
        _reference_kind = in.readUnsignedByte();
        _reference_index = in.readUnsignedShort();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeByte(_reference_kind);
        out.writeShort(_reference_index);
    }
    
    public int getReferenceKind() {
        return _reference_kind;
    }
    
    public void setReferenceKind(int referenceKind) throws IllegalArgumentException {
        if (referenceKind < 1 || referenceKind > 9) {
            throw new IllegalArgumentException("MethodHandle referencekind cannot accept a value of " + referenceKind);
        }
        
        _reference_kind = referenceKind;
    }
    
    /**
     * The Entry Type depends on both the reference kind and the Class Version (CV).
     * 
     * 1 (REF_getField), 2 (REF_getStatic), 3 (REF_putField), or 4 (REF_putStatic) - CONSTANT_Fieldref_info
     * 5 (REF_invokeVirtual) or 8 (REF_newInvokeSpecial) - CONSTANT_Methodref_info
     * 6 (REF_invokeStatic) or 7 (REF_invokeSpecial) 
     *    - If CV < 52:  CONSTANT_Methodref_info
     *    - if CV >= 52: CONSTANT_Methodref_info or CONSTANT_InterfaceMethodref_info
     * 9 (REF_invokeInterface) - CONSTANT_InterfaceMethodref_info
     * 
     * @return
     */
    public Entry getReference() {
        return getPool().getEntry(_reference_index);
    }
    
    public void setReference(int referenceIndex) {
        _reference_index = referenceIndex;
    }

}
