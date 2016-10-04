package software.cm.crazytower.errores;

public class ExcepcionGeneral extends Exception {
    public ExcepcionGeneral() {
    }

    public ExcepcionGeneral(String detailMessage) {
        super(detailMessage);
    }

    public ExcepcionGeneral(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ExcepcionGeneral(Throwable throwable) {
        super(throwable);
    }
}
