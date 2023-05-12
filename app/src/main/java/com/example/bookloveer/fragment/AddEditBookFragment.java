package com.example.bookloveer.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookloveer.R;
import com.example.bookloveer.adapter.BookItemAdapter;
import com.example.bookloveer.model.Book;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddEditBookFragment extends Fragment {

    private String bookId;
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etTitle, etAuthor, etImageUrl, etDescription;
    private Button btnSaveBook;
    private RecyclerView rvSuggestedBooks;
    private BookItemAdapter suggestedBookAdapter;
    private Book bookToUpdate;
    private boolean uploaded = false;

    public AddEditBookFragment() {
    }

    public static AddEditBookFragment newInstance(Book book) {
        AddEditBookFragment fragment = new AddEditBookFragment();
        Bundle args = new Bundle();
        args.putSerializable("book", book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        etTitle = view.findViewById(R.id.et_title);
        etAuthor = view.findViewById(R.id.et_author);
        etImageUrl = view.findViewById(R.id.et_image_url);
        etDescription = view.findViewById(R.id.et_description);

        rvSuggestedBooks = view.findViewById(R.id.rv_suggested_books);
        btnSaveBook = view.findViewById(R.id.btn_save_book);

        List<Book> suggestedBooks = new ArrayList<>();
        suggestedBooks.add(new Book("3", "https://i0.wp.com/americanwritersmuseum.org/wp-content/uploads/2018/02/CK-3.jpg?resize=267%2C400", "To Kill a Mockingbird", "Harper Lee", "To Kill a Mockingbird is a novel by Harper Lee published in 1960."));
        suggestedBooks.add(new Book("4", "https://m.media-amazon.com/images/I/819ijTWp9JL.jpg", "1984", "George Orwell", "Nineteen Eighty-Four, often published as 1984, is a dystopian social science fiction novel by the English novelist George Orwell."));

        suggestedBookAdapter = new BookItemAdapter(suggestedBooks, getActivity());
        rvSuggestedBooks.setAdapter(suggestedBookAdapter);
        rvSuggestedBooks.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (getArguments() != null) {
            bookToUpdate = (Book) getArguments().getSerializable("book");
            etImageUrl.setText(bookToUpdate.getImage());
            etTitle.setText(bookToUpdate.getTitle());
            etAuthor.setText(bookToUpdate.getAuthor());
            etDescription.setText(bookToUpdate.getDescription());
            btnSaveBook.setText("Update");
        }
        etImageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
                // why fragment close after file done chosing

            }
        });

        btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String image = etImageUrl.getText().toString();
                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();
                String description = etDescription.getText().toString();

                // Get a reference to the Firebase Storage instance
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://book-lover-8bffc.appspot.com");
                StorageReference storageRef = storage.getReference();

                if (bookToUpdate == null) {
                    // Generate a unique id for the book
                    DatabaseReference booksRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("books");
                    bookId = booksRef.push().getKey();

                    // Create a new Book object with the entered data and the generated id
                    Book book = new Book(bookId, "", title, author, description);

                    // Save the book to the database
                    booksRef.child(bookId).setValue(book);

                    // Upload the image to Firebase Storage
                    if (!image.isEmpty()) {
                        Uri fileUri = Uri.parse(image);
                        StorageReference imageRef = storageRef.child("books/" + bookId + "/image.jpg");
                        UploadTask uploadTask = imageRef.putFile(fileUri);

                        // Add an onComplete listener to get the download URL and update the book's image field in the database
                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return imageRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String imageUrl = downloadUri.toString();

                                    // Update the book's image field in the database with the download URL
                                    booksRef.child(bookId).child("image").setValue(imageUrl);
                                    uploaded = true;
                                } else {
                                    // Handle errors
                                    Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    bookId = bookToUpdate.getId();
                    // Update the existing book with the entered data
                    bookToUpdate.setImage(image);
                    bookToUpdate.setTitle(title);
                    bookToUpdate.setAuthor(author);
                    bookToUpdate.setDescription(description);

                    // Save the updated book to the database
                    DatabaseReference booksRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("books");
                    booksRef.child(bookToUpdate.getId()).setValue(bookToUpdate);

                    // Upload the image to Firebase Storage
                    if (!image.isEmpty() && !uploaded) {
                        Uri fileUri = Uri.parse(image);
                        if (isValidFileUri(fileUri)) {
                            // Get a reference to the Firebase Storage instance
                            storage = FirebaseStorage.getInstance("gs://book-lover-8bffc.appspot.com");
                            storageRef = storage.getReference();

                            StorageReference imageRef = storageRef.child("books/" + bookId + "/image.jpg");
                            UploadTask uploadTask = imageRef.putFile(fileUri);

                            // Add an onComplete listener to get the download URL and update the book's image field in the database
                            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return imageRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        String imageUrl = downloadUri.toString();

                                        // Update the book's image field in the database with the download URL
                                        DatabaseReference booksRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("books");
                                        booksRef.child(bookId).child("image").setValue(imageUrl);
                                    } else {
                                        // Handle errors
                                        Log.w("TAG", "onComplete: Failed=" + task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            // If it is not a local URI, use the image URL as is
                            booksRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("books");
                            booksRef.child(bookId).child("image").setValue(image);
                        }
                    } else {
                        // If no image was provided, set the image field to an empty string
                        booksRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("books");
                        booksRef.child(bookId).child("image").setValue("");
                    }
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            // Update the etImageUrl field with the selected image URI
            etImageUrl.setText(imageUri.toString());
        }
    }

    private boolean isValidFileUri(Uri uri) {
        // Check if the URI scheme is either "file" or "content"
        final String scheme = uri.getScheme();
        if(scheme == null || (!scheme.equals(ContentResolver.SCHEME_FILE) && !scheme.equals(ContentResolver.SCHEME_CONTENT))) {
            return false;
        }

        try {
            // Try to open a stream with the given URI
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            if(inputStream != null) {
                // If the stream was successfully opened, close it and return true
                inputStream.close();
                return true;
            }
        } catch (IOException e) {
            // If an exception occurred while trying to open the stream, return false
            return false;
        }

        // If we get here, the URI is not a valid file
        return false;
    }
}