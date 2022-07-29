package db_project.view.viewUtils;

import javafx.scene.control.CheckBox;

public class StationWithCheckBox {
  private String name;
  private CheckBox select;

  public StationWithCheckBox(String name) {
    this.name = name;
    this.select = new CheckBox();
    this.select.setSelected(true);
  }

  public CheckBox getSelect() {
    return this.select;
  }

  public void setSelect(CheckBox select) {
    this.select = select;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
