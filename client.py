import getpass
from External_service import CloudDeliveries
from pprint import pprint
import os

def print_loginmenu():
	email = input("Username : ")
	password = getpass.getpass("Enter Password : ")
	return email, password

def print_menu():
	print("1 - Drivers")
	print("2 - Type Veichle")
	print("3 - Veichle")
	print("4 - Order")
	option = input("option : ")
	return option

def print_submenu():
	print("1 - Get")
	print("2 - Add")
	print("3 - update")
	print("4 - Delete")
	option = input("option : ")
	return option

def print_submenu_order():
	print("1 - Get User Orders")
	print("2 - Update State")
	option = input("option : ")
	return option

deliveries = CloudDeliveries()
email, password = print_loginmenu()
dic = deliveries.authenticate(email, password)
code, token = dic['status'], dic['token'].decode()
while code != 200:
	email, password = print_loginmenu()
	dic = deliveries.authenticate(email, password)
	code, token = dic['status'], dic['token'].decode()

#print(token)
#os.system("cls")
option = "0"
while option != "10":
	
	option = print_menu()
	os.system("cls")
	if option == "1":		# Drivers
		sub_option = print_submenu()

		if sub_option == "1":
			drivers = deliveries.getDrivers()
			print([driver.toJSON() for driver in drivers])
		elif sub_option == "2":
			contact = input("introduza o contacto : ")
			deliveries.addDriver(contact)
		elif sub_option == "3":
			idd = input("Introduza o id : ")
			contact = input("introduza o contacto : ")
			deliveries.updateDriver(idd, contact)
		else:
			idd = input("Introduza o id : ")
			deliveries.deleteDriver(idd)

	elif option == "2":
		sub_option = print_submenu()

		if sub_option == "1":
			veichle_types = deliveries.getVeichleTypes()
			print([v.toJSON() for v in veichle_types])
		elif sub_option == "2":
			name = input("introduza o nome : ")
			capacidade = input("Introduza a capacidade : ")
			deliveries.addVeichleType(name, capacidade)
		elif sub_option == "3":
			idd = input("Introduza o id : ")
			name = input("introduza o nome : ")
			capacidade = input("Introduza a capacidade : ")
			deliveries.updateVeichleType(idd, name, capacidade)
		else:
			idd = input("Introduza o id : ")
			deliveries.deleteVeichleType(idd)

	elif option == '3':
		sub_option = print_submenu()

		if sub_option == "1":
			veichles = deliveries.getVeichles()
			print([v.toJSON() for v in veichles])
		elif sub_option == "2":
			id_type = input("introduza o id do tipo de veículo : ")
			id_driver = input("Introduza o id do Driver : ")
			deliveries.addVeichle(id_type, id_driver)
		elif sub_option == "3":
			idd = input("Introduza o id : ")
			id_type = input("introduza o id do tipo de veículo : ")
			id_driver = input("Introduza o id do Driver : ")
			deliveries.updateVeichle(idd, id_type, id_driver)
		else:
			idd = input("Introduza o id : ")
			deliveries.deleteVeichle(idd)

	elif option == '4':
		sub_option = print_submenu_order()

		if sub_option == '1':
			email = input("Introduza o email : ")
			orders = deliveries.getOrders(email)
			print(orders)
			
		elif sub_option == '2':
			idd = input("introduza o id da order : ")
			state = input("Introduza o state da order (progress, deliver, finish) : ")
			while not state in ['progress', 'deliver', 'finish']:
				state = input("Introduza o state da order (progress, deliver, finish) : ")

			deliveries.updateOrderState(idd, state)

	print()

deliveries.logout()