package api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Task;

import java.io.IOException;


import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        jsonWriter.value(Task.durationToStream(duration));

    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        return Task.durationFromStream(jsonReader.nextString());
    }

}
