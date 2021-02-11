<svelte:head>
	<title>Flight Planning</title>
</svelte:head>

<script>
	import { TextField, Select } from 'svelte-materialify/src';
	import CardForm from '../components/CardForm.svelte';

	const aircrafts = [
    { name: 'AirCraft1', value: 'AirCraft1' },
    { name: 'AirCraft2', value: 'AirCraft2' },
  ];
	let valid = false;
	let airportCode = "TTAB789";
	let airportPlannedFor = "";

	$: formdata = {
		schedule: {
			departure: {
				airport: {
					code: airportCode,
					plannedFor: airportPlannedFor
				}
			}
		},
		// Selected aircraft manufacturerSpecification
		manufacturerSpecification: {
			manufacturer: "EMBRAER",
			model: "E2-190",
			serialNumber: 283201230
		},
		// Selected aircraft carrier
		carrier: {
			name: "TAM",
			type: "AIRLINE"
		}
	}
</script>

<CardForm title="Flight Planning" prevLink="inventory" nextLink="fleet-crew" isNextDisabled={false}>
	<Select outlined items={aircrafts}>Aircraft</Select>
	<TextField outlined>Departure Date</TextField>
	<TextField outlined>Departure Airport</TextField>
	<TextField outlined>Arrival Date</TextField>
	<TextField outlined>Arrival Airport</TextField>
</CardForm>