package nguyen.huy.moneylover.Tool;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseTool {
    public static DatabaseReference baseReference()
    {
        return FirebaseDatabase.getInstance().getReference();
    }
    public static String getUserId()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
