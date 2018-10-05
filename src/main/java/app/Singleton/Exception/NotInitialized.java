package app.Singleton.Exception;

public class NotInitialized extends SinletoneException {
    public NotInitialized(String SinletoneModule) {
        super(String.format("not initialized %s", SinletoneModule));
    }
}
