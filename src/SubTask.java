class SubTask extends Task {

    private Epic master;

    public SubTask(String n, String d) {
        super(n, d);
    }

    public Epic getMaster() {
        return master;
    }

    public void setMaster(Epic master) {
        this.master = master;
    }
    @Override
    public String toString() {
        String s = super.toString();
        return String.format("%s master=%s",s,master == null ? "null":master.getName());
    }

}
