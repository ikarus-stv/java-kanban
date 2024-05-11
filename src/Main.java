public class Main {

    public static void main(String[] args) {

        TaskManager tm = new TaskManager();

        Task task1 = new Task("Задача 1", "Описание задачи 1");
        tm.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        tm.createTask(task2);

        Epic epic1=new Epic("Эпик 1", "Описание эпика 1");
        tm.createEpic(epic1);

        Subtask st11 = new Subtask("Подзадача 1.1", "Описание 1.1", epic1.getId());
        tm.createSubtask(st11);
        Subtask st12 = new Subtask("Подзадача 1.2", "Описание 1.2", epic1.getId());
        tm.createSubtask(st12);



        Epic epic2=new Epic("Эпик 2", "Описание эпика 2");
        tm.createEpic(epic2);

        Subtask st21 = new Subtask("Подзадача 2.1", "Описание 2.1", epic2.getId());
        tm.createSubtask(st21);

        System.out.println("Задачи:");
        System.out.println(tm.getAllTasks());
        System.out.println("Эпики:");
        System.out.println(tm.getAllEpics());
        System.out.println("Подзадачи:");
        System.out.println(tm.getAllSubtasks());

        task1.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateTask(task1);
        task2.setStatus(TaskStatus.DONE);
        tm.updateTask(task2);

        System.out.println("Статус задачи 1 стал "+tm.getTaskById(task1.getId()).getStatus());
        System.out.println("Статус задачи 2 стал "+tm.getTaskById(task2.getId()).getStatus());

        st11.setStatus(TaskStatus.IN_PROGRESS);
        st12.setStatus(TaskStatus.DONE);
        tm.updateSubtask(st11);
        tm.updateSubtask(st12);

        System.out.println("Статус подзадачи 1.1 стал "+tm.getSubtaskById(st11.getId()).getStatus());
        System.out.println("Статус задачи 1.2 стал "+tm.getSubtaskById(st12.getId()).getStatus());
        System.out.println("Статус эпика 1 стал "+tm.getEpicById(epic1.getId()).getStatus());

        st11.setStatus(TaskStatus.DONE);
        tm.updateSubtask(st11);
        System.out.println("Статус подзадачи 1.1 стал "+tm.getSubtaskById(st11.getId()).getStatus());
        System.out.println("Статус эпика 1 стал "+tm.getEpicById(epic1.getId()).getStatus());

        if (tm.deleteTask(task1.getId()))
            System.out.println("Удалили задачу 1");
        if (tm.deleteEpic(epic1.getId()))
            System.out.println("Удалили эпик 1");

        System.out.println("Задачи:");
        System.out.println(tm.getAllTasks());
        System.out.println("Эпики:");
        System.out.println(tm.getAllEpics());
        System.out.println("Подзадачи:");
        System.out.println(tm.getAllSubtasks());

    }
}
