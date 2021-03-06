package com.example.owensbikes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import com.squareup.sdk.register.RegisterClient;

/**
 * Class for displaying user-facing dialogs.
 */
public class DialogComposer {

  private static final String REGISTER_PACKAGE = "com.squareup";
  private static final Uri OAUTH_URI = Uri.parse(
      "https://connect.squareup.com/oauth2/authorize?client_id="
          + BuildConfig.CLIENT_ID
          + "&amp;scope=PAYMENTS_WRITE");
  private static final Uri OAUTH_DOC_URI =
      Uri.parse("https://docs.connect.squareup.com/api/oauth/#navsection-oauth");

  private final MainActivity activity;
  private final RegisterClient registerClient;

  DialogComposer(MainActivity activity, RegisterClient registerClient) {
    this.activity = activity;
    this.registerClient = registerClient;
  }

  public void showOAuthAlertDialog() {
    new AlertDialog.Builder(activity).setTitle(R.string.error_not_authorized)
        .setMessage(activity.getResources().getString(R.string.error_oauth_message))
        .setPositiveButton(R.string.learn_more, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            // If the user clicks "Learn More", direct them to the OAuth documentation on
            // connect.squareup.com
            Intent oAuthDocIntent = new Intent(Intent.ACTION_VIEW, OAUTH_DOC_URI);
            oAuthDocIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(oAuthDocIntent);
          }
        })
        .setNegativeButton(R.string.start_oauth, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            // If the user wants a token now, give them the OAuth link with a redirect URI that
            // goes to a GitHub page with more information.
            Intent oAuthIntent = new Intent(Intent.ACTION_VIEW, OAUTH_URI);
            activity.startActivity(oAuthIntent);
          }
        })
        .setNeutralButton(R.string.ok, null)
        .show();
  }

  public void showErrorDialog(int titleResId, int messageResId) {
    AlertDialog.Builder errorAlertBuilder = new AlertDialog.Builder(activity).setTitle(titleResId)
        .setMessage(messageResId)
        .setPositiveButton(activity.getString(R.string.ok), null);
    errorAlertBuilder.show();
  }

  public void showErrorDialogWithRetry(int titleResId, int messageResId) {
    new AlertDialog.Builder(activity).setTitle(titleResId)
        .setMessage(messageResId)
        .setNegativeButton(activity.getString(R.string.ok), null)
        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            activity.checkout();
          }
        })
        .show();
  }

  public void showRegisterUninstalledDialog() {
    new AlertDialog.Builder(activity).setTitle(R.string.error_install_register)
        .setMessage(activity.getString(R.string.error_install_register_message))
        .setPositiveButton(activity.getString(R.string.install_register_confirm),
            new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {
                registerClient.openRegisterPlayStoreListing();
              }
            })
        .setNegativeButton(R.string.cancel, null)
        .show();
  }

  public void showTransactionInProgressDialog() {
    new AlertDialog.Builder(activity).setTitle(R.string.error_transaction_in_progress)
        .setMessage(R.string.error_transaction_in_progress_message)
        .setPositiveButton(R.string.open_register, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            // Start Register as if opened from the Android launcher, which should open to
            // the transaction in progress.
            PackageManager packageManager = activity.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(REGISTER_PACKAGE);
            activity.startActivity(intent);
          }
        })
        .setNegativeButton(R.string.cancel, null)
        .show();
  }

  public void showUpdateRegisterDialog() {
    new AlertDialog.Builder(activity).setTitle(R.string.update_register_title)
        .setMessage(R.string.update_register_message)
        .setPositiveButton(activity.getString(R.string.install_register_confirm),
            new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {
                registerClient.openRegisterPlayStoreListing();
              }
            })
        .setNegativeButton(R.string.cancel, null)
        .show();
  }
}
