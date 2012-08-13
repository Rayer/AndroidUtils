package com.rayer.util.intent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class ReceiverExecuter extends BroadcastReceiver {

  Activity hostActivity;
  Map<String, Method> responserMap = new HashMap<String, Method>();
  IMethodActionConvertor defaultActionMethodConv;
  
  Object invokeTarget;

  public interface REMonitor {
    void onRegisterMethodSucceed(String actionName, String methodName);
    void onRegisterFailed(String actionName, String methodName);
    void onNoActionRegistered();
  }

  REMonitor listener;
  protected Activity getHostActivity() {
    return hostActivity;
  }

  public ReceiverExecuter(Activity activity, Object inInvokeTarget) {
    super();
    invokeTarget = inInvokeTarget;
    init(activity);
    
  }

  public ReceiverExecuter(Activity activity, Object inInvokeTarget, REMonitor rem) {
    super();
    invokeTarget = inInvokeTarget;
    setMonitorListener(rem);
    init(activity);
  }

  public void checkIntegrentacny(String... actions) {
    for (String action : actions)
      if (responserMap.get(action) == null) {
        Log.e("Jiepang", "Not registered action : " + action);
        Log.e("Jiepang", "Please declare and implement : "
            + defaultActionMethodConv.getActionMethodName(action));
      }
  }

  /**
   * @param activity
   */
  private void init(Activity activity) {
    hostActivity = activity;

    defaultActionMethodConv = new IMethodActionConvertor() {

      @Override
      public String getActionMethodName(String action) {
        String converted = action.replaceAll("_", "__");
        converted = converted.replaceAll("\\.", "_");
        return "public void " + converted + "(Context context, Intent intent);";
      }

      @Override
      public String transmuteActionFromName(String name) {
        name = name.replaceAll("_", ".");
        return name.replaceAll("\\.\\.", "_");
      }

    };

    Method[] methods = getInvokeTarget().getClass().getDeclaredMethods();
    // Log.d("Jiepang", "methods size = " + methods.length);

    for (Method m : methods) {
      m.setAccessible(true);
      RExecuterAttr attr = m.getAnnotation(RExecuterAttr.class);

      if (attr == null)
        continue;

      String[] handleAction = attr.handleAction();
      registerMethod(m, handleAction);

    }
    
    if(responserMap.isEmpty())
      if(listener != null)
        listener.onNoActionRegistered();

  }
  
  private Object getInvokeTarget() {
    return invokeTarget == null ? this : invokeTarget;
  }

  public void setMonitorListener(REMonitor rem) {
    listener = rem;
  }

  static final Class<?>[] STANDARD_CALLBACK_SIGNATURE = {Context.class, Intent.class};
  public void registerMethod(Method m, String[] handleAction) {

    for (int count = 0; count < handleAction.length; ++count) {
      String responseTo = handleAction[count].equals(RExecuterAttr.DEF_HANDLE_ACTION_NONE) ? defaultActionMethodConv
          .transmuteActionFromName(m.getName()) : handleAction[count];

      
      //2 signature allowed : (Context, Intent) or ()
      Class<?>[] type = m.getParameterTypes();
      if ((type == null || type.length != 2 || type[0] != Context.class
          || type[1] != Intent.class) && type.length != 0) {
        if (listener != null)
          listener.onRegisterFailed(responseTo, m.getName());
        continue;
      }
      if (listener != null)
        listener.onRegisterMethodSucceed(responseTo, m.getName());
      responserMap.put(responseTo, m);
      
      hostActivity.registerReceiver(this, new IntentFilter(responseTo));
        
    }

  }

  public void release() {
    Log.d("Jiepang", "Releasing reciever!" + this.toString());
    if(responserMap.isEmpty() == false)
      hostActivity.unregisterReceiver(this);
  }

  @Override
  public void onReceive(Context paramContext, Intent paramIntent) {

    Log.d("Jiepang", "On receive : " + paramIntent.getAction());
    String action = paramIntent.getAction();
    Method m = responserMap.get(action);

    // 需要做一個error警告嗎?
    if (m == null)
      return;

    Log.d("Jiepang", "Match found : " + m.getName());

    try {
      if(m.getParameterTypes().length == 2)
        m.invoke(getInvokeTarget(), paramContext, paramIntent);
      else
        m.invoke(getInvokeTarget());
      
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

  }
  
  static private REMonitor msDefInst;
  public static REMonitor getDefaultMonitor() {
    if(msDefInst == null)
      msDefInst = new REMonitorDefault();
    
    return msDefInst;
  }
  
  static public class REMonitorDefault implements REMonitor {
    
    //private final Logger logger = Logger.getInstance(this.getClass());

    @Override
    public void onRegisterMethodSucceed(String actionName, String methodName) {
      //logger.i("Successed register action : " + actionName + " to method : " + methodName);
    }

    @Override
    public void onRegisterFailed(String actionName, String methodName) {
      //logger.e("Error register action : " + actionName + " to method : " + methodName);
    }

    @Override
    public void onNoActionRegistered() {
      //logger.e("No action was registered in ReceiverExecuter!");
    }
    
    

 
  }

}
