/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.iscreate.op.service.rno.job.avro.proto;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class VmInfoList extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"VmInfoList\",\"namespace\":\"com.iscreate.op.service.rno.job.avro.proto\",\"fields\":[{\"name\":\"vmlist\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"VmInfo\",\"fields\":[{\"name\":\"nodeType\",\"type\":\"string\",\"default\":\"WorkerNode\"},{\"name\":\"createTime\",\"type\":\"long\"},{\"name\":\"startTime\",\"type\":\"long\"},{\"name\":\"isFixed\",\"type\":\"boolean\"},{\"name\":\"name\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"token\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"state\",\"type\":\"string\",\"default\":\"\"}]}}]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.util.List<com.iscreate.op.service.rno.job.avro.proto.VmInfo> vmlist;

  /**
   * Default constructor.
   */
  public VmInfoList() {}

  /**
   * All-args constructor.
   */
  public VmInfoList(java.util.List<com.iscreate.op.service.rno.job.avro.proto.VmInfo> vmlist) {
    this.vmlist = vmlist;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return vmlist;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: vmlist = (java.util.List<com.iscreate.op.service.rno.job.avro.proto.VmInfo>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'vmlist' field.
   */
  public java.util.List<com.iscreate.op.service.rno.job.avro.proto.VmInfo> getVmlist() {
    return vmlist;
  }

  /**
   * Sets the value of the 'vmlist' field.
   * @param value the value to set.
   */
  public void setVmlist(java.util.List<com.iscreate.op.service.rno.job.avro.proto.VmInfo> value) {
    this.vmlist = value;
  }

  /** Creates a new VmInfoList RecordBuilder */
  public static com.iscreate.op.service.rno.job.avro.proto.VmInfoList.Builder newBuilder() {
    return new com.iscreate.op.service.rno.job.avro.proto.VmInfoList.Builder();
  }
  
  /** Creates a new VmInfoList RecordBuilder by copying an existing Builder */
  public static com.iscreate.op.service.rno.job.avro.proto.VmInfoList.Builder newBuilder(com.iscreate.op.service.rno.job.avro.proto.VmInfoList.Builder other) {
    return new com.iscreate.op.service.rno.job.avro.proto.VmInfoList.Builder(other);
  }
  
  /** Creates a new VmInfoList RecordBuilder by copying an existing VmInfoList instance */
  public static com.iscreate.op.service.rno.job.avro.proto.VmInfoList.Builder newBuilder(com.iscreate.op.service.rno.job.avro.proto.VmInfoList other) {
    return new com.iscreate.op.service.rno.job.avro.proto.VmInfoList.Builder(other);
  }
  
  /**
   * RecordBuilder for VmInfoList instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<VmInfoList>
    implements org.apache.avro.data.RecordBuilder<VmInfoList> {

    private java.util.List<com.iscreate.op.service.rno.job.avro.proto.VmInfo> vmlist;

    /** Creates a new Builder */
    private Builder() {
      super(com.iscreate.op.service.rno.job.avro.proto.VmInfoList.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.iscreate.op.service.rno.job.avro.proto.VmInfoList.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing VmInfoList instance */
    private Builder(com.iscreate.op.service.rno.job.avro.proto.VmInfoList other) {
            super(com.iscreate.op.service.rno.job.avro.proto.VmInfoList.SCHEMA$);
      if (isValidValue(fields()[0], other.vmlist)) {
        this.vmlist = data().deepCopy(fields()[0].schema(), other.vmlist);
        fieldSetFlags()[0] = true;
      }
    }

    /** Gets the value of the 'vmlist' field */
    public java.util.List<com.iscreate.op.service.rno.job.avro.proto.VmInfo> getVmlist() {
      return vmlist;
    }
    
    /** Sets the value of the 'vmlist' field */
    public com.iscreate.op.service.rno.job.avro.proto.VmInfoList.Builder setVmlist(java.util.List<com.iscreate.op.service.rno.job.avro.proto.VmInfo> value) {
      validate(fields()[0], value);
      this.vmlist = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'vmlist' field has been set */
    public boolean hasVmlist() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'vmlist' field */
    public com.iscreate.op.service.rno.job.avro.proto.VmInfoList.Builder clearVmlist() {
      vmlist = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    public VmInfoList build() {
      try {
        VmInfoList record = new VmInfoList();
        record.vmlist = fieldSetFlags()[0] ? this.vmlist : (java.util.List<com.iscreate.op.service.rno.job.avro.proto.VmInfo>) defaultValue(fields()[0]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
