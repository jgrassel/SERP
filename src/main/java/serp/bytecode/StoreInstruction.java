package serp.bytecode;

import java.io.*;

import serp.bytecode.visitor.*;

/**
 * An instruction to store a value from a local variable onto the stack.
 *
 * @author Abe White
 */
public class StoreInstruction extends LocalVariableInstruction {
    private static final Class[][] _mappings = new Class[][] {
        { byte.class, int.class },
        { boolean.class, int.class },
        { char.class, int.class },
        { short.class, int.class },
        { void.class, int.class },
    };
    String _type = null;

    StoreInstruction(Code owner) {
        super(owner);
    }

    StoreInstruction(Code owner, int opcode) {
        super(owner, opcode);
    }

    int getLength() {
        switch (getOpcode()) {
        case Constants.ISTORE:
        case Constants.LSTORE:
        case Constants.FSTORE:
        case Constants.DSTORE:
        case Constants.ASTORE:
            return super.getLength() + 1;
        default:
            return super.getLength();
        }
    }

    public int getLogicalStackChange() {
        switch (getOpcode()) {
        case Constants.NOP:
            return 0;
        default:
            return -1;
        }
    }

    public int getStackChange() {
        switch (getOpcode()) {
        case Constants.LSTORE:
        case Constants.LSTORE0:
        case Constants.LSTORE1:
        case Constants.LSTORE2:
        case Constants.LSTORE3:
        case Constants.DSTORE:
        case Constants.DSTORE0:
        case Constants.DSTORE1:
        case Constants.DSTORE2:
        case Constants.DSTORE3:
            return -2;
        case Constants.NOP:
            return 0;
        default:
            return -1;
        }
    }

    public String getTypeName() {
        switch (getOpcode()) {
        case Constants.ISTORE:
        case Constants.ISTORE0:
        case Constants.ISTORE1:
        case Constants.ISTORE2:
        case Constants.ISTORE3:
            return int.class.getName();
        case Constants.LSTORE:
        case Constants.LSTORE0:
        case Constants.LSTORE1:
        case Constants.LSTORE2:
        case Constants.LSTORE3:
            return long.class.getName();
        case Constants.FSTORE:
        case Constants.FSTORE0:
        case Constants.FSTORE1:
        case Constants.FSTORE2:
        case Constants.FSTORE3:
            return float.class.getName();
        case Constants.DSTORE:
        case Constants.DSTORE0:
        case Constants.DSTORE1:
        case Constants.DSTORE2:
        case Constants.DSTORE3:
            return double.class.getName();
        case Constants.ASTORE:
        case Constants.ASTORE0:
        case Constants.ASTORE1:
        case Constants.ASTORE2:
        case Constants.ASTORE3:
            return Object.class.getName();
        default:
            return _type;
        }
    }

    public TypedInstruction setType(String type) {
        type = mapType(type, _mappings, true);
        int local = getLocal();
        int len = getLength();

        // if an invalid type or local, revert to nop
        if ((type == null) || (local < 0)) {
            _type = type;
            setOpcode(Constants.NOP);
        } else {
            // valid opcode, unset saved type
            _type = null;
            switch (type.charAt(0)) {
            case 'i':
                setOpcode((local > 3) ? Constants.ISTORE
                    : (Constants.ISTORE0 + local));
                break;
            case 'l':
                setOpcode((local > 3) ? Constants.LSTORE
                    : (Constants.LSTORE0 + local));
                break;
            case 'f':
                setOpcode((local > 3) ? Constants.FSTORE
                    : (Constants.FSTORE0 + local));
                break;
            case 'd':
                setOpcode((local > 3) ? Constants.DSTORE
                    : (Constants.DSTORE0 + local));
                break;
            default:
                setOpcode((local > 3) ? Constants.ASTORE
                    : (Constants.ASTORE0 + local));
                break;
            }
        }
        if (len != getLength())
            invalidateByteIndexes();
        return this;
    }

    /**
     * StoreInstructions are equal if the type they reference the same
     * type and locals index or if either is unset.
     */
    public boolean equalsInstruction(Instruction other) {
        if (other == this)
            return true;
        if (!super.equalsInstruction(other))
            return false;

        String type = getTypeName();
        String otherType = ((StoreInstruction) other).getTypeName();
        return type == null || otherType == null || type.equals(otherType);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterStoreInstruction(this);
        visit.exitStoreInstruction(this);
    }

    void read(Instruction orig) {
        super.read(orig);
        StoreInstruction ins = (StoreInstruction) orig;
        _type = ins._type;
    }

    void read(DataInput in) throws IOException {
        super.read(in);
        switch (getOpcode()) {
        case Constants.ISTORE:
        case Constants.LSTORE:
        case Constants.FSTORE:
        case Constants.DSTORE:
        case Constants.ASTORE:
            setLocal(in.readUnsignedByte());
            break;
        }
    }

    void write(DataOutput out) throws IOException {
        super.write(out);
        switch (getOpcode()) {
        case Constants.ISTORE:
        case Constants.LSTORE:
        case Constants.FSTORE:
        case Constants.DSTORE:
        case Constants.ASTORE:
            out.writeByte(getLocal());
        }
    }

    void calculateOpcode() {
        // taken care of when setting type
        setType(getTypeName());
    }

    void calculateLocal() {
        switch (getOpcode()) {
        case Constants.ISTORE0:
        case Constants.LSTORE0:
        case Constants.FSTORE0:
        case Constants.DSTORE0:
        case Constants.ASTORE0:
            setLocal(0);
            break;
        case Constants.ISTORE1:
        case Constants.LSTORE1:
        case Constants.FSTORE1:
        case Constants.DSTORE1:
        case Constants.ASTORE1:
            setLocal(1);
            break;
        case Constants.ISTORE2:
        case Constants.LSTORE2:
        case Constants.FSTORE2:
        case Constants.DSTORE2:
        case Constants.ASTORE2:
            setLocal(2);
            break;
        case Constants.ISTORE3:
        case Constants.LSTORE3:
        case Constants.FSTORE3:
        case Constants.DSTORE3:
        case Constants.ASTORE3:
            setLocal(3);
            break;
        }
    }
}
