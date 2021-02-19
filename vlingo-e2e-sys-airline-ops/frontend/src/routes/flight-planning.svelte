<svelte:head>
	<title>Flight Planning</title>
</svelte:head>

<script>
	import CardForm from '../components/CardForm.svelte';
	import { TextField } from 'svelte-materialify/src';
	import { Api } from "../api";
	import { inventories, flights } from "../stores";
	import { required } from "../util/validators.js";
	import VlSelect from '../components/VlSelect.svelte';

	let valid = false;
	let selectedAircraft = "";
	let cardForm;

	let formData = {
		schedule: {
			departure: {
				airport: {
					code: "TTAB789",
					plannedFor: 1613066697198
				}
			},
			arrival: {
				airport: {
					code: "TTAB12",
					plannedFor: 1613066797198
				}
			}
		},
		manufacturerSpecification: {
			manufacturer: "EMBRAER",
			model: "E2-190",
			serialNumber: "283201230"
		},
		carrier: {    
			name: "TAM",    
			type: "AIRLINE"
		}
	}

	const submit= async () => {
		const res = await Api.post("/flight-plannings/", formData);
		if (res) {
			$flights = [...$flights, res];
			cardForm.toggleDialog();
		}
	}

	$: aircrafts = $inventories.map((inv => {
		return {
			name: inv.manufacturerSpecification.manufacturer,
			value: inv.id,
			...inv
		};
	}));

	$: if (selectedAircraft) {
			const { manufacturerSpecification, carrier } = aircrafts.find(airc => airc.id === selectedAircraft);
			formData.manufacturerSpecification = manufacturerSpecification;
			formData.carrier = carrier;
		}

	$: valid = !!selectedAircraft && !!formData.schedule.departure.airport.code && !!formData.schedule.departure.airport.plannedFor && !!formData.schedule.arrival.airport.code && !!formData.schedule.arrival.airport.plannedFor;
</script>

<CardForm
	title="Flight Planning"
	buttonText="New Flight Planning"
	prevLink="inventory"
	nextLink="fleet-crew"
	isNextDisabled={$flights.length < 1}
	showNoContent={$flights.length < 1}
	{valid}
	on:submit={submit}
	bind:this={cardForm}
>
	<table class="mb-6">
		<thead>
			<tr>
				<th>#</th>
				<th>Departure Airport Code</th>
				<th>Arrival Airport Code</th>
				<th>Cancelled</th>
				<th>#</th>
			</tr>
		</thead>
		<tbody>
			{#each $flights as flight, ind (flight.id)}
				<tr>
					<td>{ind + 1}</td>
					<td>{flight.schedule.departure.airport.code}</td>
					<td>{flight.schedule.arrival.airport.code}</td>
					<td>{flight.cancelled}</td>
					<td></td>
				</tr>
			{/each}
		</tbody>
	</table>
	<div slot="dialog-form">
		<VlSelect outlined rules={[required]} items={aircrafts} bind:value={selectedAircraft}>Aircraft</VlSelect>
		<TextField outlined rules={[required]} bind:value={formData.schedule.departure.airport.code}>Departure Airport Code</TextField>
		<TextField outlined rules={[required]} bind:value={formData.schedule.departure.airport.plannedFor}>Departure Airport Planned For</TextField>
		<TextField outlined rules={[required]} bind:value={formData.schedule.arrival.airport.code}>Arrival Airport Code</TextField>
		<TextField outlined rules={[required]} bind:value={formData.schedule.arrival.airport.plannedFor}>Arrival Airport Planned For</TextField>
	</div>
</CardForm>