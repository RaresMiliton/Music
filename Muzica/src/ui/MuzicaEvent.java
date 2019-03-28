package ui;

import domain.Muzica;

public class MuzicaEvent implements Event {
    public Muzica getOldData() {
        return oldData;
    }

    public void setOldData(Muzica oldData) {
        this.oldData = oldData;
    }

    public Muzica getData() {
        return data;
    }

    public void setData(Muzica data) {
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public void setType(ChangeEventType type) {
        this.type = type;
    }

    public MuzicaEvent(Muzica oldData, Muzica data, ChangeEventType type) {
        this.oldData = oldData;
        this.data = data;
        this.type = type;
    }

    private Muzica oldData;
    private Muzica data;
    private ChangeEventType type;
}
