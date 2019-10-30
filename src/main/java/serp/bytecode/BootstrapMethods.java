package serp.bytecode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import serp.bytecode.visitor.BCVisitor;

/*
 * BootstrapMethods_attribute {
 *     u2 attribute_name_index;
 *     u4 attribute_length;
 *     u2 num_bootstrap_methods;
 *     {   u2 bootstrap_method_ref;
 *         u2 num_bootstrap_arguments;
 *         u2 bootstrap_arguments[num_bootstrap_arguments];
 *     } bootstrap_methods[num_bootstrap_methods];
 * }
 */
public class BootstrapMethods extends Attribute {
    private BootstrapMethodElement[] _bootstrapMethods = new BootstrapMethodElement[0];
    
    BootstrapMethods(int nameIndex, Attributes owner) {
        super(nameIndex, owner);
    }

    public void acceptVisit(BCVisitor visitor) {
        visitor.enterBootstrapMethod(this);
        visitor.exitBootstrapMethod(this);        
    }
    
    public int getNumberBootstrapMethods() {
        return _bootstrapMethods.length;
    }
    
    public BootstrapMethodElement[] getBootstrapMethods() {
        BootstrapMethodElement[] retval = new BootstrapMethodElement[_bootstrapMethods.length];
        for (int i = 0; i < _bootstrapMethods.length; i++) {
            retval[i] = _bootstrapMethods[i];
        }
        
        return retval;
    }
    
    public void setBootstrapMethods(BootstrapMethodElement[] methods) {
        if (methods == null || methods.length == 0) {
            _bootstrapMethods = new BootstrapMethodElement[0];
            return;
        }
        
        _bootstrapMethods = new BootstrapMethodElement[methods.length];
        
        for (int i = 0; i < methods.length; i++) {
            _bootstrapMethods[i] = methods[i];
        }
    }
    
    /**
     * Return the length of the bytecode representation of this attribute
     * in bytes, excluding the name index.
     */
    int getLength() {
        int length = 2;
        
        for (int i = 0; i < _bootstrapMethods.length; i++) {
            length += _bootstrapMethods[i].getLength();
        }
        
        return length;
    }

    void read(DataInput in, int length) throws IOException {
        int num_bootstrap_methods = in.readShort();
        _bootstrapMethods = new BootstrapMethodElement[num_bootstrap_methods];
        
        for (int i = 0; i < num_bootstrap_methods; i++) {
            _bootstrapMethods[i] = new BootstrapMethodElement(this, in);
        }
    }

    void write(DataOutput out, int length) throws IOException {
        out.writeShort(_bootstrapMethods.length);
        
        for (int i = 0; i < _bootstrapMethods.length; i++) {
            _bootstrapMethods[i].write(out);
        }
    }
}
