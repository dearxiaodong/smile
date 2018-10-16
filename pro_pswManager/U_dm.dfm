object DM: TDM
  OldCreateOrder = False
  OnCreate = DataModuleCreate
  Left = 898
  Top = 379
  Height = 123
  Width = 168
  object con1: TADOConnection
    ConnectionString = 
      'Provider=MSDASQL.1;Password=root;Persist Security Info=True;User' +
      ' ID=root;Data Source=mysqlOdbc'
    LoginPrompt = False
    Provider = 'MSDASQL.1'
    Left = 32
    Top = 8
  end
end
