package android.source;

public class FeedbackStatus
{
  public static int DISCONNECT;
  public static int FAILED;
  public static int SUCCEED = 0;

  static
  {
    FAILED = 1;
    DISCONNECT = 2;
  }
}

