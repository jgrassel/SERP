package serp.bytecode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

import serp.bytecode.lowlevel.Entry;
import serp.bytecode.lowlevel.MethodHandleEntry;

// {   u2 bootstrap_method_ref;
//     u2 num_bootstrap_arguments;
//     u2 bootstrap_arguments[num_bootstrap_arguments];
//  bootstrap_methods[num_bootstrap_methods];
public class BootstrapMethodElement {
    private BootstrapMethods _bootstrapMethodAttribute;
    
    private int _bootstrap_method_ref = 0;
    private int[] _bootstrap_arguments = new int[0];
    
    public BootstrapMethodElement() {
        
    }

    public BootstrapMethodElement(BootstrapMethods bootstrapmethodAttr, int bootstrap_method_ref, 
            int num_bootstrap_arguments, int[] bootstrap_arguments) {
        super();
        _bootstrapMethodAttribute = bootstrapmethodAttr;
        _bootstrap_method_ref = bootstrap_method_ref;
       
        _bootstrap_arguments = new int[num_bootstrap_arguments];
        
        for (int i = 0; i < num_bootstrap_arguments; i++) {
            _bootstrap_arguments[i] = bootstrap_arguments[i];
        }
    }
    
    public BootstrapMethodElement(BootstrapMethods bootstrapmethodAttr, DataInput in) throws IOException {
        _bootstrapMethodAttribute = bootstrapmethodAttr;
        
        _bootstrap_method_ref = in.readShort();
        
        int num_bootstrap_arguments = in.readShort();
        _bootstrap_arguments = new int[num_bootstrap_arguments];
        
        for (int i = 0; i < num_bootstrap_arguments; i++) {
            _bootstrap_arguments[i] = in.readShort();
        }
    }

    public BootstrapMethods getBootstrapMethodAttribute() {
        return _bootstrapMethodAttribute;
    }

    public void setBootstrapMethodAttribute(BootstrapMethods bootstrapMethodAttribute) {
        _bootstrapMethodAttribute = bootstrapMethodAttribute;
    }

    
    public int getBootstrapMethodRef() {
        return _bootstrap_method_ref;
    }
    
    public MethodHandleEntry getBootstrapMethod() {
        if (_bootstrap_method_ref == 0) {
            return null;
        }
        
        Entry e = _bootstrapMethodAttribute.getPool().getEntry(_bootstrap_method_ref);
        return (MethodHandleEntry) e;
    }

    public void setBootstrapMethodRef(int bootstrap_method_ref) {
        _bootstrap_method_ref = bootstrap_method_ref;
    }
    
    public void setBootstrapMethod(MethodHandleEntry mhe) {
        if (mhe == null) {
            _bootstrap_method_ref = 0;
            return;
        }
        
        _bootstrap_method_ref = mhe.getIndex();
    }

    
    public int getNumBootstrapArguments() {
        return _bootstrap_arguments.length;
    }
    
    public int[] getBootstrapArgumentIndices() {
        return Arrays.copyOf(_bootstrap_arguments, _bootstrap_arguments.length);
    }
    
    public Entry[] getBootstrapArguments() {
        Entry[] ceArr = new Entry[getNumBootstrapArguments()];
        for (int i = 0; i < ceArr.length; i++) {
            ceArr[i] = _bootstrapMethodAttribute.getPool().getEntry(_bootstrap_arguments[i]);
        }
        return ceArr;
    }

    public void setBootstrapArgumentIndices(int[] bootstrap_arguments) {
        if (bootstrap_arguments == null || bootstrap_arguments.length == 0) {
            _bootstrap_arguments = new int[0];
            return;
        }
        
        _bootstrap_arguments = Arrays.copyOf(bootstrap_arguments, bootstrap_arguments.length); 
    }
    
    public void setBootstrapArguments(Entry[] bsArgs) {
        if (bsArgs == null || bsArgs.length == 0) {
            _bootstrap_arguments = new int[0];
            return;
        }
        
        _bootstrap_arguments = new int[bsArgs.length];
        
        for (int i = 0; i < bsArgs.length; i++) {
            _bootstrap_arguments[i] = bsArgs[i].getIndex();
        }
    }
    
    public int getLength() {
        return 4 + (2 * _bootstrap_arguments.length);
    }
    
    public void write(DataOutput out) throws IOException {
        out.writeShort(_bootstrap_method_ref);
        out.writeShort(_bootstrap_arguments.length);
        
        for (int i = 0; i < _bootstrap_arguments.length; i++) {
            out.writeShort(_bootstrap_arguments[i]);
        }
    }
}
