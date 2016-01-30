package com.huaren.logistics.recycleinput;

import com.huaren.logistics.bean.RecycleInput;

public interface IRecycleInputDetailInputView {
  void showDialog(String title, String message, RecycleInput recycleInput);

  void enterRecycleInput();
}
