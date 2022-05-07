package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.DistribuicaoMacrosModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MacrosUpdateForm {

	@NotNull
	private Long id;
	@NotNull //@Length(min = 1,max = 4)
	private double carboidrato;
	@NotNull //@Length(min = 1,max = 4)
	private double proteina;
	@NotNull //@Length(min = 1,max = 4)
	private double gordura;

	public DistribuicaoMacrosModel converter() {
		return new DistribuicaoMacrosModel(id,carboidrato,proteina,gordura);
	}

}
