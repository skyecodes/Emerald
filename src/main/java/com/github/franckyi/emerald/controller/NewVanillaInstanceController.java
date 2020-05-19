package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.service.task.instance.VanillaInstanceCreatorTask;
import com.github.franckyi.emerald.service.web.resource.mojang.VersionManifest;
import com.github.franckyi.emerald.view.animation.SmoothScrolling;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.skins.JFXTreeTableViewSkin;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Region;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class NewVanillaInstanceController extends MenuController<Region, VersionManifest> {
    @FXML
    private JFXTextField instanceNameField;
    @FXML
    private JFXTreeTableView<VersionItem> versionTableView;
    @FXML
    private JFXTreeTableColumn<VersionItem, Node> latestColumn;
    @FXML
    private JFXTreeTableColumn<VersionItem, String> versionColumn;
    @FXML
    private JFXTreeTableColumn<VersionItem, String> typeColumn;
    @FXML
    private JFXTreeTableColumn<VersionItem, String> dateColumn;
    @FXML
    private JFXCheckBox showReleasesCheckbox;
    @FXML
    private JFXCheckBox showSnapshotsCheckbox;
    @FXML
    private JFXCheckBox showBetaVersionsCheckbox;
    @FXML
    private JFXCheckBox showAlphaVersionsCheckbox;
    @FXML
    private JFXButton createInstanceButton;

    @Override
    protected void initialize() {
        SmoothScrolling.apply(versionTableView);
        versionTableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin instanceof JFXTreeTableViewSkin) {
                TableHeaderRow header = ((JFXTreeTableViewSkin<?>) newSkin).getTableHeaderRow();
                header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
                header.setPrefHeight(30);
            }
        });
        versionTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        latestColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("latest"));
        versionColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("version"));
        typeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("date"));
        showReleasesCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> this.updatePredicate());
        showSnapshotsCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> this.updatePredicate());
        showBetaVersionsCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> this.updatePredicate());
        showAlphaVersionsCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> this.updatePredicate());
        createInstanceButton.disableProperty().bind(instanceNameField.textProperty().isEmpty().or(
                versionTableView.getSelectionModel().selectedItemProperty().isNull()));
    }

    @Override
    public void beforeShowing() {
        super.beforeShowing();
        instanceNameField.clear();
        showReleasesCheckbox.setSelected(true);
        showSnapshotsCheckbox.setSelected(false);
        showBetaVersionsCheckbox.setSelected(false);
        showAlphaVersionsCheckbox.setSelected(false);
    }

    @Override
    protected void modelUpdated() {
        List<VersionItem> collect = this.getModel().getVersions().stream()
                .map(version -> new VersionItem(this.getModel().getLatest(), version))
                .collect(Collectors.toList());
        ObservableList<VersionItem> list = FXCollections.observableList(collect);
        versionTableView.setRoot(new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren));
        this.updatePredicate();
    }

    private void updatePredicate() {
        versionTableView.getSelectionModel().clearSelection();
        versionTableView.setPredicate(this::getPredicate);
    }

    private boolean getPredicate(TreeItem<VersionItem> item) {
        VersionManifest.Version v = item.getValue().get();
        switch (v.getType()) {
            case RELEASE:
                return showReleasesCheckbox.isSelected();
            case SNAPSHOT:
                return showSnapshotsCheckbox.isSelected();
            case BETA:
                return showBetaVersionsCheckbox.isSelected();
            case ALPHA:
                return showAlphaVersionsCheckbox.isSelected();
        }
        return false;
    }

    @FXML
    private void backAction() {
        this.getMainController().showPrevious();
    }

    @FXML
    private void homeAction() {
        this.getMainController().showHome();
    }

    @FXML
    private void createInstanceAction() {
        this.getMainController().showHome();
        this.getMainController().createNewInstance(new VanillaInstanceCreatorTask(instanceNameField.getText(),
                versionTableView.getSelectionModel().getSelectedItem().getValue().get()));
    }

    public static class VersionItem extends RecursiveTreeObject<VersionItem> {
        private final VersionManifest.Version version;
        private final Node latest;

        public VersionItem(VersionManifest.Latest latest, VersionManifest.Version version) {
            this.version = version;
            if (latest.getSnapshot().equals(version.getId())) {
                this.latest = new MaterialIconView(MaterialIcon.BUG_REPORT);
            } else if (latest.getRelease().equals(version.getId())) {
                this.latest = new MaterialIconView(MaterialIcon.STAR);
            } else {
                this.latest = null;
            }
        }

        public Node getLatest() {
            return latest;
        }

        public String getVersion() {
            return version.getId();
        }

        public String getType() {
            return version.getType().name().toLowerCase();
        }

        public String getDate() {
            return DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.systemDefault()).format(version.getReleaseTime());
        }

        public VersionManifest.Version get() {
            return version;
        }
    }
}
