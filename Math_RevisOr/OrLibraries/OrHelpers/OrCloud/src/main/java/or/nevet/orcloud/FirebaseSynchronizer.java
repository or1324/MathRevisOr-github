package or.nevet.orcloud;


import com.google.android.gms.tasks.Task;

import java.util.concurrent.CountDownLatch;

class FirebaseSynchronizer<T> {

    //This method blocks the current thread. It needs to be called from a new thread.
    public T waitForTaskToFinish(Task<T> task) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Task<T> result = task.addOnCompleteListener(task1 -> latch.countDown());
        latch.await();
        if (result.isSuccessful())
            return result.getResult();
        throw result.getException();
    }
}
