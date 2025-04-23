import sys
import requests
from PyQt5.QtWidgets import QApplication, QMainWindow, QTableWidget, QTableWidgetItem, QVBoxLayout, QWidget, QPushButton, QMessageBox

class DataDisplayWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("Device Data Display")
        self.setGeometry(100, 100, 600, 400)

        # 后端 API 地址
        self.api_url = "http://localhost:8081/api/device-data"

        # 创建主窗口部件和布局
        central_widget = QWidget()
        self.setCentralWidget(central_widget)
        layout = QVBoxLayout(central_widget)

        # 创建表格
        self.table = QTableWidget()
        self.table.setColumnCount(3)
        self.table.setHorizontalHeaderLabels(["ID", "Temperature", "Timestamp"])

        # 创建刷新按钮
        self.refresh_button = QPushButton("Refresh Data")
        self.refresh_button.clicked.connect(self.refresh_data)

        # 添加控件到布局
        layout.addWidget(self.refresh_button)
        layout.addWidget(self.table)

        # 初始加载数据
        self.refresh_data()

    def fetch_data(self):
        """从后端获取数据"""
        try:
            response = requests.get(self.api_url)
            response.raise_for_status()  # 检查请求是否成功
            return response.json()  # 返回 JSON 数据
        except requests.RequestException as e:
            QMessageBox.critical(self, "Error", f"Failed to fetch data: {e}")
            return []

    def update_table(self, data):
        """更新表格内容"""
        self.table.setRowCount(len(data))
        for row_idx, item in enumerate(data):
            self.table.setItem(row_idx, 0, QTableWidgetItem(str(item.get("id", ""))))
            self.table.setItem(row_idx, 1, QTableWidgetItem(item.get("temperature", "")))
            self.table.setItem(row_idx, 2, QTableWidgetItem(item.get("timestamp", "")))
        self.table.resizeColumnsToContents()
        self.table.setEditTriggers(QTableWidget.NoEditTriggers)  # 禁止编辑
        self.table.setSelectionMode(QTableWidget.SingleSelection)  # 单选模式

    def refresh_data(self):
        """刷新数据"""
        data = self.fetch_data()
        self.update_table(data)

if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = DataDisplayWindow()
    window.show()
    sys.exit(app.exec_())