package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.HttpTaskManager;
import manager.ManagerLoadException;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import tasks.Task;
import test.TaskManagerTest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {



    @BeforeEach
    @Override
    public void setUpTaskManager() throws IOException, ManagerLoadException {
        kvServer = Managers.getDefaultKVServer();
        manager = new HttpTaskManager(KVServer.PORT);
        super.setUpTaskManager();

    }



    @Test
    protected void load() throws IOException, InterruptedException {
       manager.getTaskById(task.getId());

    }
}