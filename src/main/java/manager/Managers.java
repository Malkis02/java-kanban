package manager;

import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {
    public static TaskManager getDefault() {
        //return new FileBackedTasksManager("data.csv");
        return new HttpTaskManager(KVServer.PORT);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static KVServer getDefaultKVServer() throws IOException {
        return new KVServer();
    }
    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();

    }
}
