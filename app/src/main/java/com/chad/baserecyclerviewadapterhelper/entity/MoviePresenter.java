package com.chad.baserecyclerviewadapterhelper.entity;

import android.view.View;
import android.widget.Toast;

public class MoviePresenter {

  public void buyTicket(View view, Movie movie) {
    Toast.makeText(view.getContext(), "buy ticket: " + movie.name, Toast.LENGTH_SHORT).show();
  }
}
