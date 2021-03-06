package com.example.taskapp.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.MainActivity;
import com.example.taskapp.R;
import com.example.taskapp.TaskAdapter;
import com.example.taskapp.models.Task;
import com.example.taskapp.ui.interfaces.OnItemClickListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private TaskAdapter taskAdapter;
    ArrayList<Task> list;
    private int currentPos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initList(view);
        initResultlistener();

    }

    private void initResultlistener() {
        getParentFragmentManager().setFragmentResultListener("form", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e("home", "onFragmentResult");
                Task task = (Task) result.getSerializable("task");
                boolean edit = result.getBoolean("edit");
                if (edit){
                    list.set(currentPos,task);
                }else {
                    list.add(0, task);
                }
                taskAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        if (list == null) {
            list = new ArrayList<>();
            list.add(new Task("Talgar", 0L));
            list.add(new Task("Tilek", 0L));
            list.add(new Task("Aza", 0L));
            list.add(new Task("fgj", 0L));
            list.add(new Task("Talfdhfgar", 0L));
            list.add(new Task("Talfhntrggar", 0L));
        }
        taskAdapter = new TaskAdapter(list);
        recyclerView.setAdapter(taskAdapter);
        taskAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItem(int position) {
                currentPos = position;
                Task task = list.get(position);
                openForm(task);
            }

            @Override
            public void onItemLongClick(int position) {
                currentPos = position;
                showAlert(list.get(position));
            }

            private void showAlert(Task task) {
                AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity());
                // заголовок
                adb.setTitle(R.string.exit);
                // сообщение
                adb.setMessage(R.string.save_data);
                // иконка
                adb.setIcon(android.R.drawable.ic_dialog_info);
                // кнопка положительного ответа
                adb.setPositiveButton(R.string.yes, myClickListener);
                // кнопка отрицательного ответа
                adb.setNegativeButton(R.string.no, myClickListener);
                // кнопка нейтрального ответа
                adb.setNeutralButton(R.string.cancel, myClickListener);
                // создаем диалог
                adb.create();
                adb.show();
            }
        });
        
    }
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    list.remove(currentPos);
                    taskAdapter.notifyDataSetChanged();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            openForm(null);
        }
        return super.onOptionsItemSelected(item);

    }
    private void openForm(Task task){
        Bundle bundle= new Bundle();
        bundle.putSerializable("task",task);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_navigation_home_to_formFragment,bundle);
    }
}
