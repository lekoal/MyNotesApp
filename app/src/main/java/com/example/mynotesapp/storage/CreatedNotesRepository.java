package com.example.mynotesapp.storage;

import android.os.Handler;
import android.os.Looper;

import com.example.mynotesapp.domain.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreatedNotesRepository implements NotesRepository {

    ArrayList<Note> noteList = new ArrayList<>();

    private final Executor executor = Executors.newSingleThreadExecutor();

    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private ArrayList<Note> makeList() {
        noteList.add(new Note("note1", "Note 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras augue nibh, tempus et dui id, bibendum porttitor ante. Morbi rhoncus porttitor est in porta. Suspendisse lacinia vestibulum fringilla. Aliquam erat volutpat. Integer viverra dapibus lorem, non tincidunt nibh. Pellentesque mattis eget felis ac tristique. Donec pellentesque posuere viverra.  Nunc cursus augue leo, a bibendum metus fermentum id. Aliquam nec eros elementum, tempor est quis, dignissim risus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Donec dui dolor, commodo eu justo non, vulputate molestie justo. Donec luctus accumsan dui, rutrum placerat tellus lobortis non. Phasellus nec vestibulum purus, ut lacinia sapien. Morbi at imperdiet nunc, eget mattis mi. In at lacinia est. Nulla in leo sed odio scelerisque venenatis vel lacinia eros. Maecenas efficitur malesuada mauris eu condimentum. Maecenas vel ex nec elit ultrices ultricies.  Ut rhoncus sem id enim consectetur, a laoreet quam tempus. Ut at felis tristique, efficitur libero id, placerat nisl. Mauris porta eget felis eget cursus. Aliquam semper neque pulvinar ex consequat consequat vel et lectus. Proin et augue a tortor consequat auctor. Cras lorem ligula, ultricies vel lectus vel, tempus pretium enim. Sed sed odio vitae dolor tincidunt volutpat vel sit amet mi. Aenean ultrices, nulla sed tincidunt interdum, orci erat aliquet nisl, in sollicitudin est magna sit amet ipsum. Duis nec massa nibh. Pellentesque augue turpis, consequat in rutrum gravida, ullamcorper a quam. Morbi id nibh quis felis dignissim mollis. Curabitur tempus, velit sed condimentum dictum, neque mauris cursus nisi, et commodo nisi sem vel metus. Aliquam et accumsan magna. In pulvinar sodales felis non sagittis.  Phasellus vitae diam mollis, consequat neque ac, aliquet dolor. Donec a imperdiet tortor. Nullam sit amet massa finibus, tristique augue eget, volutpat sem. Curabitur et mollis mauris. Proin commodo pellentesque cursus. Quisque aliquet nunc id velit aliquet, eu commodo tellus tempus. Phasellus sit amet urna sapien. Suspendisse mattis suscipit venenatis. Fusce tincidunt, est id tristique sagittis, diam ante ornare tortor, id pharetra est ex venenatis enim. Fusce erat sapien, viverra nec commodo vitae, volutpat nec metus. Nulla sed purus et lectus viverra faucibus. In porta vel lacus sed porta.  Donec metus lacus, facilisis nec iaculis id, dapibus nec nulla. Sed malesuada ut felis sed ornare. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nullam nec eros egestas, dapibus ligula vitae, tincidunt dui. Maecenas accumsan justo erat, in dignissim lacus molestie scelerisque. Suspendisse ornare nec mi non tristique. Nam in mauris rhoncus, viverra arcu nec, pulvinar massa.  Sed porta consectetur odio in convallis. Vivamus porttitor dolor eget nisi vulputate volutpat. Donec dictum consequat sollicitudin. Phasellus consequat gravida est et lacinia. Donec fringilla felis vitae enim commodo pellentesque. Quisque nisl quam, rutrum in felis eu, viverra scelerisque elit. Sed faucibus urna dictum, lacinia nibh sit amet, aliquet eros. Aliquam nibh lorem, aliquet sed scelerisque non, bibendum non dolor.  Aenean ultricies sodales dolor non feugiat. In eleifend hendrerit dolor, vel aliquet leo pulvinar non. Donec sollicitudin risus sed semper egestas. Pellentesque convallis dictum arcu ut ullamcorper. Phasellus pellentesque malesuada gravida. Nunc egestas sem libero, eu dignissim nunc tempor accumsan. Pellentesque lorem arcu, ultricies at pulvinar non, imperdiet in purus.  Aliquam sit amet porttitor diam, maximus pharetra elit. Morbi nec posuere orci. Aenean molestie felis id ultrices sollicitudin. Suspendisse sed condimentum tortor. Phasellus elementum justo pharetra risus mattis, a pulvinar justo pretium. Sed volutpat lacinia rutrum. Maecenas eu dolor cursus, convallis felis in, iaculis turpis. Donec velit tellus, bibendum eu mi eu, varius venenatis diam. Nulla semper elit felis, id ullamcorper elit auctor sit amet. "));
        noteList.add(new Note("note2", "Note 2", "Morbi sed placerat felis, sit amet rhoncus sapien. Ut pharetra eu mi sed ullamcorper."));
        noteList.add(new Note("note3", "Note 3", "Nulla vitae dictum orci. Pellentesque consequat orci accumsan feugiat porta."));
        noteList.add(new Note("note4", "Note 4", "Fusce erat erat, iaculis in tincidunt eu, malesuada eu urna."));
        noteList.add(new Note("note5", "Note 5", "Vivamus vitae mi bibendum, posuere diam eget, sodales augue. Proin consectetur turpis at lorem pharetra tincidunt."));
        noteList.add(new Note("note6", "Note 6", "Mauris dapibus a ipsum sit amet consequat. Phasellus ut erat ut ante fringilla volutpat. Nunc sed facilisis risus."));
        noteList.add(new Note("note7", "Note 7", "Nam eget hendrerit mauris. Duis et felis et ex rhoncus iaculis sit amet sed enim."));
        noteList.add(new Note("note8", "Note 8", "Etiam nec quam rhoncus magna malesuada vestibulum in non massa."));
        return noteList;
    }

    @Override
    public void getNotes(Callback<List<Note>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(makeList());
                    }
                });
            }
        });
    }

    @Override
    public void clear(Callback<Void> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                noteList.clear();

                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(null);
                    }
                });
            }
        });
    }

    @Override
    public void add(String title, String content, Callback<Note> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Note note = new Note(UUID.randomUUID().toString(), title, content);
                noteList.add(note);

                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(note);
                    }
                });
            }
        });
    }

    @Override
    public void update(String id, String title, String content, Callback<Note> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Note note = new Note(id, title, content);
                noteList.add(note);

                int index = -1;

                for (int i = 0; i < noteList.size(); i++) {
                    if (noteList.get(i).getId().equals(id)) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    noteList.remove(index);
                    noteList.set(index, note);
                }

                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(note);
                    }
                });
            }
        });
    }

    @Override
    public void delete(Note note, Callback<Void> callback) {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        noteList.remove(note);

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(null);
            }
        });
    }
}
