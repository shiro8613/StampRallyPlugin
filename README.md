**MYSQLを使うときは自分でデータベースとユーザーを作ってね♡**

```mysql
CREATE USER 'stamp'@'%' IDENTIFIED BY 'stamppasswd';
CREATE DATABASE stampll;
GRANT ALL PRIVILEGES ON stampll.* TO 'stamp'@'%' WITH GRANT OPTION;
```
