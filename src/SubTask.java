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
        //       return "Task{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", " + "id=" + id + ", status='" + status + '\'' + '}';
        String s = super.toString(); // получаем строку состояния от предка(но уже с приставкой Epic)
        return String.format("%s master=%s",s,master == null ? "null":master.getName());
    }

}
