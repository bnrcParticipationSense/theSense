package com.bupt.bnrc.thesenser.interfaces;

public interface PMPredictSubject {
	public void registerObserver(PMPredictObserver o);
	public void removeObserver(PMPredictObserver o);
	public void notifyObservers();
}
