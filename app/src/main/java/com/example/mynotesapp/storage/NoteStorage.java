package com.example.mynotesapp.storage;

import com.example.mynotesapp.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteStorage implements NotesRepository {

    @Override
    public List<Note> getNotes() {
        ArrayList<Note> noteList = new ArrayList<>();
        noteList.add(new Note("Note 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed consectetur porta mi et laoreet."));
        noteList.add(new Note("Note 2", "Morbi sed placerat felis, sit amet rhoncus sapien. Ut pharetra eu mi sed ullamcorper."));
        noteList.add(new Note("Note 3", "Nulla vitae dictum orci. Pellentesque consequat orci accumsan feugiat porta."));
        noteList.add(new Note("Note 4", "Fusce erat erat, iaculis in tincidunt eu, malesuada eu urna."));
        noteList.add(new Note("Note 5", "Vivamus vitae mi bibendum, posuere diam eget, sodales augue. Proin consectetur turpis at lorem pharetra tincidunt."));
        noteList.add(new Note("Note 6", "Mauris dapibus a ipsum sit amet consequat. Phasellus ut erat ut ante fringilla volutpat. Nunc sed facilisis risus."));
        noteList.add(new Note("Note 7", "Nam eget hendrerit mauris. Duis et felis et ex rhoncus iaculis sit amet sed enim."));
        noteList.add(new Note("Note 8", "Etiam nec quam rhoncus magna malesuada vestibulum in non massa."));
        return noteList;
    }
}
