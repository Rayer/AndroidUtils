package com.rayer.util.provisioner;

import com.rayer.util.network.DownloadManager;

public abstract class ManagedInternetResourceProvisioner<T> extends
		InternetResourceProvisioner<T> {

	abstract DownloadManager getDownloadManager();

}
